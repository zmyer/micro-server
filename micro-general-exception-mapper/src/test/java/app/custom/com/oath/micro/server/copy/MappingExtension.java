package app.custom.com.oath.micro.server.copy;

import java.util.LinkedHashMap;

import javax.ws.rs.core.Response.Status;

import cyclops.data.tuple.Tuple;
import cyclops.data.tuple.Tuple2;
import org.springframework.stereotype.Component;

import com.oath.micro.server.general.exception.mapper.ExtensionMapOfExceptionsToErrorCodes;

@Component
public class MappingExtension implements ExtensionMapOfExceptionsToErrorCodes {

	@Override
	public LinkedHashMap<Class<? extends Exception>, Tuple2<String, Status>> getErrorMappings() {
		LinkedHashMap<Class<? extends Exception>, Tuple2<String, Status>> map = new LinkedHashMap<>();
		map.put(MyException.class, Tuple.tuple("my-error",Status.BAD_GATEWAY));
		return map;
	}

}
