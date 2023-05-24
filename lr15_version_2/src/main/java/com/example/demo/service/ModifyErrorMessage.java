package com.example.demo.service;

import com.example.demo.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("ModifyErrorMessage")
public class ModifyErrorMessage implements MyModifyService {
    public Response modify (Response response) {
        response.setErrorMessage("Что-то сломалось");
        return response;
    }
}
