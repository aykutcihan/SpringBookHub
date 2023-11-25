package com.project.springbookhub.payload.response;

import com.project.springbookhub.payload.response.abstracts.BaseUserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class AdminResponse extends BaseUserResponse {
}
