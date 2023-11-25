package com.project.springbookhub.payload.request;

import com.project.springbookhub.payload.request.abstracts.BaseUserRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ClientRequest extends BaseUserRequest {
}
