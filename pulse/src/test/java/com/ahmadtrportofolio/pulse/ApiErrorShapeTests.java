package com.ahmadtrportofolio.pulse;

import com.ahmadtrportofolio.pulse.common.GlobalExceptionHandler;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiErrorShapeTests {

    static class Bean {
        @jakarta.validation.constraints.NotNull
        Double x;
        public Double getX() { return x; }
        public void setX(Double x) { this.x = x; }
    }

    private MethodArgumentNotValidException validationException() throws Exception {
        Method m = Bean.class.getMethod("toString");
        MethodParameter mp = new MethodParameter(m, -1);
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(new Bean(), "bean");
        br.rejectValue("x", "NotNull", "must not be null");
        return new MethodArgumentNotValidException(mp, br);
    }

    @Test
    void validationErrorHasDetail() throws Exception {
        ProblemDetail pd = new GlobalExceptionHandler().validation(validationException());
        assertNotNull(pd.getDetail());
        assertTrue(pd.getDetail().contains("x"));
    }

    @Test
    void unreadableErrorHasDetail() {
        org.springframework.http.converter.HttpMessageNotReadableException ex =
                new org.springframework.http.converter.HttpMessageNotReadableException("malformed",
                        new org.springframework.mock.http.MockHttpInputMessage(new byte[0]));
        ProblemDetail pd = new GlobalExceptionHandler().unreadable(ex);
        assertNotNull(pd.getDetail());
    }

    @Test
    void unexpectedErrorHasDetail() {
        ProblemDetail pd = new GlobalExceptionHandler().unexpected(new RuntimeException("boom"));
        assertNotNull(pd.getDetail());
    }
}
