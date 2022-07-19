package com.jmwood.sample.discgolfreview.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionRollup implements Serializable {

    private Map<String, SessionActivity> sessionActivityMap;
}
