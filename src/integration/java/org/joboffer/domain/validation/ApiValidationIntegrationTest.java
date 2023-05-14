package org.joboffer.domain.validation;

import org.hamcrest.Matchers;
import org.joboffer.domain.BaseIntegrationTest;
import org.joboffer.domain.offer.dto.OfferDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class ApiValidationIntegrationTest extends BaseIntegrationTest {

    @Test
    @WithMockUser
    void should_throw_an_MethodArgumentNotValidException_with_every_error_message_when_user_provide_offer_with_empty_body() throws Exception {
        //given
        OfferDto notAllowedOfferDto = new OfferDto();
        //when, then
        mvc.perform(post("/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notAllowedOfferDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Matchers.is("400 BAD_REQUEST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(Matchers.is("Validation failed for object='offerDto'. Errors count: 4")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.is("2023-05-01T17:33:47.877+02:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.violations").value(Matchers.hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.violations.[*].message").value(
                        Matchers.containsInAnyOrder(
                                "Salary is mandatory and can not be null",
                                "Company is mandatory and can not be null",
                                "Title is mandatory and can not be null",
                                "OfferUrl is mandatory and can not be null")
                ))

                .andExpect(MockMvcResultMatchers.jsonPath("$.violations.[*].property").value(
                        Matchers.containsInAnyOrder(
                                "salary", "title", "company", "offerUrl")
                ))
                .andExpect(MockMvcResultMatchers.jsonPath("$.violations.[*].rejectedValue").value(
                        Matchers.contains("null", "null", "null", "null")
                ));
    }

    @Test
    @WithMockUser
    void should_throw_an_MethodArgumentNotValidException_when_user_provide_offer_with_empty_salary_field() throws Exception {
        //given, when
        mvc.perform(post("/offers")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                  {
                                    "title": "Software Engineer - Mobile (m/f/d)",
                                    "company": "Cybersource",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/software-engineer-mobile-m-f-d-cybersource-poznan-entavdpn"
                                  }
                                """.strip()))
                .andDo(MockMvcResultHandlers.print())
                //then
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Matchers.is("400 BAD_REQUEST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(Matchers.is("Validation failed for object='offerDto'. Errors count: 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.is("2023-05-01T17:33:47.877+02:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.violations").value(Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.violations.[0].message").value(Matchers.is("Salary is mandatory and can not be null")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.violations.[0].property").value(Matchers.is("salary")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.violations.[0].rejectedValue").value(Matchers.is("null")));
    }

    @Test
    @WithMockUser
    void should_throw_an_MethodArgumentNotValidException_when_user_provide_offer_with_null_salary_field() throws Exception {
        //given
        OfferDto notValidOfferWithNullSalaryField = OfferDto.builder()
                .title("Junior Java Developer")
                .company("BlueSoft Sp. z o.o.")
                .salary(null)
                .offerUrl("https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre")
                .build();

        //when, then
        String errorResponse = "Salary is mandatory and can not be null";

        mvc.perform(post("/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notValidOfferWithNullSalaryField)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Matchers.is("400 BAD_REQUEST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(Matchers.is("Validation failed for object='offerDto'. Errors count: 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.is("2023-05-01T17:33:47.877+02:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.violations").value(Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.violations.[0].message").value(Matchers.is("Salary is mandatory and can not be null")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.violations.[0].property").value(Matchers.is("salary")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.violations.[0].rejectedValue").value(Matchers.is("null")));
    }
}
