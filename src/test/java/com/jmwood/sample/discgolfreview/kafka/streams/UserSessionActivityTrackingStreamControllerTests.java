package com.jmwood.sample.discgolfreview.kafka.streams;

import com.jmwood.sample.discgolfreview.model.Course;
import com.jmwood.sample.discgolfreview.model.SessionActivity;
import com.jmwood.sample.discgolfreview.model.User;
import com.jmwood.sample.discgolfreview.model.event.*;
import com.jmwood.sample.discgolfreview.model.event.enums.AuthEventType;
import com.jmwood.sample.discgolfreview.model.event.enums.CourseEventType;
import com.jmwood.sample.discgolfreview.repository.SessionActivityRepository;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;
import org.assertj.core.util.DateUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UserSessionActivityTrackingStreamControllerTests {

  private static final String TEST_AUTH_EVENT_TOPIC = "test-auth-event-topic";
  private static final String TEST_NAV_EVENT_TOPIC = "test-nav-event-topic";
  private static final String TEST_COURSE_EVENT_TOPIC = "test-course-event-topic";
  private static final String TEST_CLICK_EVENT_TOPIC = "test-click-event-topic";
  private static final String TEST_SESSION_ACTIVITY_TOPIC = "test-session-activity-topic";
  private TopologyTestDriver topologyTestDriver;
  private TestInputTopic<String, Event> authEventTestInputTopic;
  private TestInputTopic<String, Event> navEventTestInputTopic;
  private TestInputTopic<String, Event> courseEventTestInputTopic;
  private TestInputTopic<String, Event> clickEventTestInputTopic;
  private TestOutputTopic<String, SessionActivity> sessionActivityTestOutputTopic;
  private final SessionActivityRepository mockSessionActivityRepository =
      Mockito.mock(SessionActivityRepository.class);

  private static User getSampleUser(String id) {
    return User.builder()
        .id(id)
        .name("John Doe")
        .nickname("John")
        .email("john.doe@example.com")
        .build();
  }

  private static AuthEvent getSampleLoginAuthEvent(String id, String sessionId, User user) {
    return AuthEvent.builder()
        .id(id)
        .sessionId(sessionId)
        .user(user)
        .date(DateUtil.now())
        .type(AuthEventType.LOGIN)
        .build();
  }

  private static NavEvent getSampleLoginRedirectNavEvent(String id, String sessionId, User user) {
    return NavEvent.builder()
        .id(id)
        .sessionId(sessionId)
        .user(user)
        .date(DateUtil.now())
        .path("/loginredirect")
        .rawLocationJson("{\"someJsonParam\": \"someJsonValue\"}")
        .build();
  }

  private static CourseEvent getSampleCreateCourseEvent(String id, String sessionId, User user) {
    return CourseEvent.builder()
        .id(id)
        .sessionId(sessionId)
        .user(user)
        .date(DateUtil.now())
        .course(
            Course.builder()
                .name("Sample Course")
                .location("Nowhere")
                .description("Just grass and baskets.")
                .defaultImageUrl("http://img.location.com")
                .amenities("None.")
                .author(user)
                .build())
        .rawJson("{\"someJsonParam\": \"someJsonValue\"}")
        .type(CourseEventType.ADD_NEW_COURSE)
        .build();
  }

  private static ClickEvent getSampleClickEvent(String id, String sessionId, User user) {
    return ClickEvent.builder()
        .id(id)
        .sessionId(sessionId)
        .user(user)
        .date(DateUtil.now())
        .elementName("Navbar Brand")
        .rawEventJson("{\"someJsonParam\": \"someJsonValue\"}")
        .build();
  }

  @Before
  public void setup() {
    StreamConfiguration streamConfiguration = new StreamConfiguration();
    KafkaStreamsConfiguration kafkaStreamsConfiguration =
        streamConfiguration.streamsConfiguration("testStream", "sampleServer:1111");

    UserSessionActivityTrackingStreamController streamController =
        new UserSessionActivityTrackingStreamController(
            kafkaStreamsConfiguration,
            streamConfiguration.authEventTopic(TEST_AUTH_EVENT_TOPIC),
            streamConfiguration.navEventTopic(TEST_NAV_EVENT_TOPIC),
            streamConfiguration.courseEventTopic(TEST_COURSE_EVENT_TOPIC),
            streamConfiguration.clickEventTopic(TEST_CLICK_EVENT_TOPIC),
            streamConfiguration.sessionActivityTopic(TEST_SESSION_ACTIVITY_TOPIC),
            new SessionActivityAggregator(mockSessionActivityRepository));

    Topology topology = streamController.buildTopology();

    topologyTestDriver = new TopologyTestDriver(topology, kafkaStreamsConfiguration.asProperties());

    Serde<Event> eventSerde = StreamConfiguration.getJsonSerdeForClass(Event.class);
    Serializer<Event> eventSerializer = eventSerde.serializer();

    authEventTestInputTopic =
        topologyTestDriver.createInputTopic(
            TEST_AUTH_EVENT_TOPIC, Serdes.String().serializer(), eventSerializer);
    navEventTestInputTopic =
        topologyTestDriver.createInputTopic(
            TEST_NAV_EVENT_TOPIC, Serdes.String().serializer(), eventSerializer);
    courseEventTestInputTopic =
        topologyTestDriver.createInputTopic(
            TEST_COURSE_EVENT_TOPIC, Serdes.String().serializer(), eventSerializer);
    clickEventTestInputTopic =
        topologyTestDriver.createInputTopic(
            TEST_CLICK_EVENT_TOPIC, Serdes.String().serializer(), eventSerializer);

    sessionActivityTestOutputTopic =
        topologyTestDriver.createOutputTopic(
            TEST_SESSION_ACTIVITY_TOPIC,
            Serdes.String().deserializer(),
            StreamConfiguration.getJsonSerdeForClass(SessionActivity.class).deserializer());
  }

  @After
  public void tearDown() {
    topologyTestDriver.close();
  }

  @Test
  public void verifyEventAppersInOutputTopic() {
    // given
    String sessionId = "test-session-id";
    User user = getSampleUser("test-user");
    AuthEvent testAuthEvent = getSampleLoginAuthEvent("auth-event-1", sessionId, user);

    // when
    authEventTestInputTopic.pipeInput(sessionId, testAuthEvent);

    // then
    SessionActivity expectedSessionActivity =
        new SessionActivity(sessionId, user, List.of(testAuthEvent));

    TestRecord<String, SessionActivity> actualSessionActivityRecord =
        sessionActivityTestOutputTopic.readRecord();

    assertEquals(sessionId, actualSessionActivityRecord.getKey());
    assertEquals(expectedSessionActivity, actualSessionActivityRecord.getValue());
    Mockito.verify(mockSessionActivityRepository, Mockito.times(1)).save(expectedSessionActivity);
  }

  @Test
  public void verifyEventsCombineIntoSingleSessionActivityAndVerifyEachUpdate() {
    // given
    String sessionId = "test-session-id";
    User user = getSampleUser("test-user");
    AuthEvent testAuthEvent = getSampleLoginAuthEvent("auth-event-1", sessionId, user);
    NavEvent testNavEvent = getSampleLoginRedirectNavEvent("nav-event-1", sessionId, user);

    // when
    authEventTestInputTopic.pipeInput(sessionId, testAuthEvent);
    navEventTestInputTopic.pipeInput(sessionId, testNavEvent);

    // then
    SessionActivity expectedSessionActivity =
        new SessionActivity(sessionId, user, new ArrayList<>(List.of(testAuthEvent)));

    TestRecord<String, SessionActivity> actualSessionActivityRecord =
        sessionActivityTestOutputTopic.readRecord();

    assertEquals(sessionId, actualSessionActivityRecord.getKey());
    assertEquals(expectedSessionActivity, actualSessionActivityRecord.getValue());
    Mockito.verify(mockSessionActivityRepository, Mockito.times(1)).save(expectedSessionActivity);

    expectedSessionActivity.addEvent(testNavEvent);

    actualSessionActivityRecord = sessionActivityTestOutputTopic.readRecord();

    assertEquals(sessionId, actualSessionActivityRecord.getKey());
    assertEquals(expectedSessionActivity, actualSessionActivityRecord.getValue());
    Mockito.verify(mockSessionActivityRepository, Mockito.times(1)).save(expectedSessionActivity);
  }

  @Test
  public void verifyConcurrentSessionsCreateDifferentSessionActivityObjects() {
    // given
    String sessionId_1 = "test-session-id-1";
    User user_1 = getSampleUser("user1");
    AuthEvent testAuthEvent_1 = getSampleLoginAuthEvent("auth-event-1", sessionId_1, user_1);
    NavEvent testNavEvent_1 = getSampleLoginRedirectNavEvent("nav-event-1", sessionId_1, user_1);

    String sessionId_2 = "test-session-id-2";
    User user_2 = getSampleUser("user2");
    AuthEvent testAuthEvent_2 = getSampleLoginAuthEvent("auth-event-2", sessionId_2, user_2);
    NavEvent testNavEvent_2 = getSampleLoginRedirectNavEvent("nav-event-2", sessionId_2, user_2);

    // when
    authEventTestInputTopic.pipeRecordList(
        List.of(
            new TestRecord<>(sessionId_1, testAuthEvent_1),
            new TestRecord<>(sessionId_2, testAuthEvent_2)));
    navEventTestInputTopic.pipeRecordList(
        List.of(
            new TestRecord<>(sessionId_2, testNavEvent_2),
            new TestRecord<>(sessionId_1, testNavEvent_1)));

    // then
    SessionActivity expectedSessionActivity_1 =
        new SessionActivity(sessionId_1, user_1, List.of(testAuthEvent_1, testNavEvent_1));
    SessionActivity expectedSessionActivity_2 =
        new SessionActivity(sessionId_2, user_2, List.of(testAuthEvent_2, testNavEvent_2));

    Map<String, SessionActivity> actualSessionActivityMap =
        sessionActivityTestOutputTopic.readKeyValuesToMap();
    assertEquals(
        expectedSessionActivity_1, actualSessionActivityMap.get(expectedSessionActivity_1.getId()));
    assertEquals(
        expectedSessionActivity_2, actualSessionActivityMap.get(expectedSessionActivity_2.getId()));
    Mockito.verify(mockSessionActivityRepository, Mockito.times(4)).save(Mockito.any());
  }

  @Test
  public void verifyAllEventTypesCombineToSingleSessionActivity() {
    // given
    String sessionId = "test-session-id";
    User user = getSampleUser("test-user");
    AuthEvent testAuthEvent = getSampleLoginAuthEvent("auth-event-1", sessionId, user);
    NavEvent testNavEvent = getSampleLoginRedirectNavEvent("nav-event-1", sessionId, user);
    CourseEvent testCourseEvent = getSampleCreateCourseEvent("course-event-1", sessionId, user);
    ClickEvent testClickEvent = getSampleClickEvent("click-event-1", sessionId, user);

    // when
    authEventTestInputTopic.pipeInput(sessionId, testAuthEvent);
    navEventTestInputTopic.pipeInput(sessionId, testNavEvent);
    courseEventTestInputTopic.pipeInput(sessionId, testCourseEvent);
    clickEventTestInputTopic.pipeInput(sessionId, testClickEvent);

    // then
    SessionActivity expectedSessionActivity =
        new SessionActivity(
            sessionId, user, List.of(testAuthEvent, testNavEvent, testCourseEvent, testClickEvent));

    Map<String, SessionActivity> actualSessionActivityMap =
        sessionActivityTestOutputTopic.readKeyValuesToMap();
    SessionActivity actualSessionActivity =
        actualSessionActivityMap.get(expectedSessionActivity.getId());

    assertEquals(expectedSessionActivity, actualSessionActivity);
  }
}
