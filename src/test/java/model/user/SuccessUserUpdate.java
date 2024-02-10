package model.user;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class SuccessUserUpdate{
	private String name;
	private String job;
	private String updatedAt;
}