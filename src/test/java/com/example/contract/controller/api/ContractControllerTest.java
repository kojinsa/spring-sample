package com.example.contract.controller.api;

import com.example.contract.exception.AppErrorHandler;
import com.example.contract.exception.AppException;
import com.example.contract.exception.DataNotFoundException;
import com.example.contract.domain.entity.contract.Contract;
import com.example.contract.domain.entity.contract.ContractState;
import com.example.contract.exception.ErrorCode;
import com.example.contract.mock.contract.ContractDetailImpl;
import com.example.contract.service.contract.ContractServiceImpl;
import com.example.contract.domain.mapper.ContractDetail;
import com.example.contract.dto.request.ContractSaveRequest;
import com.example.contract.dto.request.ContractUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Map;
import java.util.Optional;

import static com.example.contract.mock.ConvertUtil.convertContract;
import static com.example.contract.mock.MockUtil.asJsonString;
import static com.example.contract.mock.MockUtil.readJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("?????? ????????? ????????? ??????")
@ExtendWith(SpringExtension.class)
@WebMvcTest(ContractControllerTest.class)
class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractServiceImpl contractService;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ContractController(contractService))
                .setControllerAdvice(new AppErrorHandler())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Nested
    @DisplayName("?????? API ???")
    class CreatedAPI {

        @Test
        @DisplayName("??????????????? ????????? ?????????.")
        public void write_ok() throws Exception {

            Map mockMap = readJson("json/contract/web/write_ok.json", Map.class);

            Contract mock = convertContract((Map) mockMap.get("contract"), (Map) mockMap.get("warrant"), ContractState.NORMAL);

            given(contractService.created(any())).willReturn(mock);

            String uri = "/contract";

            ContractSaveRequest dto = readJson("json/contract/web/contract_save_request.json", ContractSaveRequest.class);

            ResultActions action = mockMvc.perform(post(uri).content(asJsonString(dto)).contentType(MediaType.APPLICATION_JSON)).andDo(print());

            then(contractService).should().created(any());

            action.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(mock.getId())).andExpect(jsonPath("$.productId").value(mock.getProduct().getId())).andExpect(jsonPath("$.warrantIds.size()").value(mock.getWarrants().size())).andExpect(jsonPath("$.term").value(mock.getTerm())).andExpect(jsonPath("$.startDate").value(mock.getStartDate())).andExpect(jsonPath("$.endDate").value(mock.getEndDate())).andExpect(jsonPath("$.premium").value(mock.getPremium())).andExpect(jsonPath("$.state").value(mock.getState().name()));
        }

        @Test
        @DisplayName("??????/????????? ?????? ??? Not_Found_Data ??? ????????? ?????????.")
        public void write_fail_1() throws Exception {

            String mock = "Product Id is 1";

            given(contractService.created(any())).willThrow(new DataNotFoundException(mock));

            String uri = "/contract";

            ContractSaveRequest dto = readJson("json/contract/web/contract_save_request.json", ContractSaveRequest.class);

            ResultActions action = mockMvc.perform(post(uri).content(asJsonString(dto)).contentType(MediaType.APPLICATION_JSON)).andDo(print());

            then(contractService).should().created(any());

            ErrorCode errorCode = ErrorCode.NOT_FOUND_DATA;

            action.andExpect(status().isNoContent()).andExpect(jsonPath("$.code").value(errorCode.getCode())).andExpect(jsonPath("$.message").value(errorCode.convertMessage(mock)));
        }

    }

    @Nested
    @DisplayName("?????? ?????? API ???")
    class DetailAPI {

        @Test
        @DisplayName("??????????????? ????????? ?????? ?????????.")
        public void getOne_ok() throws Exception {

            Optional<ContractDetail> mockOptional = Optional.of(new ContractDetailImpl(readJson("json/contract/web/getOne_ok.json", Contract.class), ContractState.NORMAL));

            given(contractService.getOne(any())).willReturn(mockOptional);

            long mockId = 1L;

            String uri = "/contracts/" + mockId;

            ResultActions action = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON)).andDo(print());

            then(contractService).should().getOne(any());

            ContractDetail mock = mockOptional.get();

            action.andExpect(status().isOk()).andExpect(jsonPath("$.term").value(mock.getTerm())).andExpect(jsonPath("$.product.range").value(mock.getProduct().getRange())).andExpect(jsonPath("$.product.title").value(mock.getProduct().getTitle())).andExpect(jsonPath("$.product.id").value(mock.getProduct().getId())).andExpect(jsonPath("$.warrants.size()").value(mock.getWarrants().size())).andExpect(jsonPath("$.startDate").value(mock.getStartDate())).andExpect(jsonPath("$.endDate").value(mock.getEndDate())).andExpect(jsonPath("$.premium").value(mock.getPremium())).andExpect(jsonPath("$.id").value(mock.getId())).andExpect(jsonPath("$.state").value(mock.getState().name()));
        }

        @Test
        @DisplayName("???????????? ?????????, No Content Http Status??? ????????? ?????? ?????????.")
        public void getOne_no_content_case() throws Exception {

            given(contractService.getOne(any())).willReturn(Optional.empty());

            long mockId = 1L;

            String uri = "/contracts/" + mockId;

            ResultActions action = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON)).andDo(print());

            then(contractService).should().getOne(any());

            action.andExpect(status().isNoContent());
        }
    }


    @Nested
    @DisplayName("?????? API ???")
    class UpdateAPI {

        @Test
        @DisplayName("??????????????? ????????? ?????????.")
        public void update_ok() throws Exception {

            Map mockMap = readJson("json/contract/web/update_ok.json", Map.class);

            Contract mock = convertContract((Map) mockMap.get("contract"), (Map) mockMap.get("warrant"), ContractState.NORMAL);

            given(contractService.update(any())).willReturn(mock);

            long mockId = 1L;

            String uri = "/contracts/" + mockId;

            ContractUpdateRequest dto = readJson("json/contract/web/contract_update_request.json", ContractUpdateRequest.class);

            ResultActions action = mockMvc.perform(
                            patch(uri)
                                    .content(asJsonString(dto))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print());

            then(contractService).should().update(any());

            action.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(mock.getId()))
                    .andExpect(jsonPath("$.productId").value(mock.getProduct().getId()))
                    .andExpect(jsonPath("$.warrantIds.size()").value(mock.getWarrants().size()))
                    .andExpect(jsonPath("$.term").value(mock.getTerm()))
                    .andExpect(jsonPath("$.startDate").value(mock.getStartDate()))
                    .andExpect(jsonPath("$.endDate").value(mock.getEndDate()))
                    .andExpect(jsonPath("$.premium").value(mock.getPremium()))
                    .andExpect(jsonPath("$.state").value(mock.getState().name()));
        }

        @Test
        @DisplayName("?????? ?????? ???????????? ?????? Server_Error ??? ????????? ?????? ?????????.")
        public void update_fail1() throws Exception {

            long mockId = 1L;

            String mock = "contract is state (Expiration) and id is " + mockId;

            given(contractService.update(any())).willThrow(new AppException(mock));

            String uri = "/contracts/" + mockId;

            ContractUpdateRequest dto = readJson("json/contract/web/contract_update_request.json", ContractUpdateRequest.class);

            ResultActions action = mockMvc.perform(
                            patch(uri)
                                    .content(asJsonString(dto))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print());

            then(contractService).should().update(any());

            ErrorCode errorCode = ErrorCode.SERVER_ERROR;

            action.andExpect(status().is5xxServerError())
                    .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                    .andExpect(jsonPath("$.message").value(errorCode.convertMessage(mock)));
        }

        @Test
        @DisplayName("?????? ???????????? ?????? ??????, Not_Found_Data ??? ????????? ?????? ?????????.")
        public void update_fail2() throws Exception {

            long mockId = 1L;

            String mock = "Product Id is " + mockId;

            given(contractService.update(any())).willThrow(new DataNotFoundException(mock));

            String uri = "/contracts/" + mockId;

            ContractUpdateRequest dto = readJson("json/contract/web/contract_update_request.json", ContractUpdateRequest.class);

            ResultActions action = mockMvc.perform(
                            patch(uri)
                                    .content(asJsonString(dto))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print());

            then(contractService).should().update(any());

            ErrorCode errorCode = ErrorCode.NOT_FOUND_DATA;

            action.andExpect(status().isNoContent())
                    .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                    .andExpect(jsonPath("$.message").value(errorCode.convertMessage(mock)));
        }
    }

}
