package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SuccessUserUpdate{
	private String name;
	private String job;
	private String updatedAt;
}