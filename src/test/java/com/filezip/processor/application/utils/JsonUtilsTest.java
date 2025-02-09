package com.filezip.processor.application.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private JsonUtils jsonUtils;

    @Test
    void testJsonToObject_Success() throws JsonProcessingException {
        String json = "{\"name\":\"John\",\"age\":30}";
        Person person = new Person("John", 30);

        when(objectMapper.readValue(json, Person.class)).thenReturn(person);
        when(objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).thenReturn(objectMapper);

        Person result = jsonUtils.jsonToObject(json, Person.class);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }

    @Test
    void testJsonToObject_JsonProcessingException() throws JsonProcessingException {
        String json = "{\"name\":\"John\",\"age\":30}";

        when(objectMapper.readValue(json, Person.class)).thenThrow(new JsonProcessingException("Mocked exception") {
        });
        when(objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).thenReturn(objectMapper);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            jsonUtils.jsonToObject(json, Person.class);
        });

        assertNotNull(exception);
    }

    static class Person {
        private String name;
        private int age;

        public Person() {
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
