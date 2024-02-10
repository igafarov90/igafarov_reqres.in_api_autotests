package model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SuccessUserCreate{
	private String createdAt;
	private String name;
	private String id;
	private String job;
}