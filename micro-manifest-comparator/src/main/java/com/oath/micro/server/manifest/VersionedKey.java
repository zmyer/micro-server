package com.oath.micro.server.manifest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.oath.micro.server.rest.jackson.JacksonUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "versioned-key")
@XmlType(name = "")
@AllArgsConstructor
@Wither
@Getter
public class VersionedKey {

	private final String key;

	private final Long version;

	public VersionedKey() {
		key = null;
		version = -1l;
	}

	public String toJson() {
		return JacksonUtil.serializeToJson(this);
	}

}
