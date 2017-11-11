package com.oath.micro.server.rest.jackson;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.oath.cyclops.util.ExceptionSoftener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.oath.micro.server.jackson.CoreJacksonConfigurator;
import com.oath.micro.server.jackson.JacksonMapperConfigurator;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public final class JacksonUtil {

	
	
	private static ObjectMapper mapper = null;

	private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);
	
	private volatile static List<JacksonMapperConfigurator>  jacksonConfigurers = 
						Arrays.asList(new CoreJacksonConfigurator(Include.NON_NULL));
	
	public static void setJacksonConfigurers(List<JacksonMapperConfigurator>  jc  )
	{
		jacksonConfigurers = jc;
		mapper = null;
		getMapper();
	}

	private synchronized static ObjectMapper createMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			
			Optional.<List<JacksonMapperConfigurator>>ofNullable(jacksonConfigurers)
						.ifPresent(list->list.forEach(a-> a.accept(mapper)));
			

		}
		return mapper;
	}
	public  static ObjectMapper getMapper() {
		if(mapper==null)
			return createMapper();
		return mapper;
	}

	public static String serializeToJsonFailSilently(final Object data) {
		try {
			return serializeToJson(data);
		} catch (Exception e) {
		}
		return "";
	}

	public static String serializeToJson(final Object data) {
		String jsonString = "";
		if (data == null)
			return jsonString;
		try {
			jsonString = getMapper().writeValueAsString(data);
		} catch (final Exception ex) {
			ExceptionSoftener.throwSoftenedException(ex);
		}
		return jsonString;
	}

	public static <T> T convertFromJson(final String jsonString, final Class<T> type) {
		try {

			
			return getMapper().readValue(jsonString, type);

		} catch (final Exception ex) {
			ExceptionSoftener.throwSoftenedException(ex);
		}
		return null;
	}
	public static <T> T convertFromJson(final String jsonString, final JavaType type) {
		try {

			
			return getMapper().readValue(jsonString, type);

		} catch (final Exception ex) {
			ExceptionSoftener.throwSoftenedException(ex);
		}
		return null;

	}
	
	public static <T> T convertFromJson(String json, final TypeReference<T> type) {
		try {
			return JacksonUtil.getMapper().readValue(json, type);
		} catch (final Exception ex) {
			ExceptionSoftener.throwSoftenedException(ex);
		}
		return null;
	}

	public static Object serializeToJsonLogFailure(Object value) {
		try {
			return serializeToJson(value);
		} catch (Exception e) {
			logger.error( e.getMessage(), e);
		}
		return value.toString();
	}

}
