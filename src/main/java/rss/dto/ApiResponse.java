package rss.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {
	
	T payload;
	
	private HttpStatus status;
	
	public ApiResponse(T payload, HttpStatus status) {
		this.payload = payload;
		this.status = status;
	}
	
}