package com.oath.micro.server.events;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JobCompleteEvent {

    private final Date date = new Date();
    private final long correlationId;
    private final String type;
    private final long errors;
    private final long dataSize;

}
