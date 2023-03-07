package com.example.contract.aop;

import com.example.contract.controller.request.WarrantSaveRequest;
import com.example.contract.dto.model.warrant.WarrantSaveModel;
import com.example.contract.service.warrant.WarrantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class IOLoggerAdvisorTest {

    private IOLoggerAdvisor ioLoggerAdvisor = mock(IOLoggerAdvisor.class);

    @Test
    public void loggingByInputAndOutput() throws Throwable {

        WarrantService warrantService = mock(WarrantService.class);

        AspectJProxyFactory factory = new AspectJProxyFactory(warrantService);

        factory.addAspect(ioLoggerAdvisor);

        WarrantService serviceProxy = factory.getProxy();

        WarrantSaveRequest req = new WarrantSaveRequest("상해치료", new BigDecimal(10000), new BigDecimal(100));

        WarrantSaveModel dto = new WarrantSaveModel(req);

        serviceProxy.created(dto);

        verify(ioLoggerAdvisor , times(1)).loggingByInputAndOutput(any());

    }

}