package com.locarie.backend.domain.dto.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.locarie.backend.datacreators.user.UserDtoCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDtoTest {
  public final ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  void registerTimeModule() {
    mapper.registerModule(new JavaTimeModule());
  }

  @Test
  void testSerialization() throws JsonProcessingException {
    UserDto dto = UserDtoCreator.businessUserDtoShreeji();
    System.out.println(mapper.writeValueAsString(dto));
  }

  @Test
  void testBusinessHoursDeserialization() throws JsonProcessingException {
    UserDto expected = UserDtoCreator.businessUserDtoShreeji();
    UserDto actual = mapper.readValue(json, UserDto.class);
    assertThat(actual.getBusinessHours())
        .usingRecursiveComparison()
        .ignoringFields("user")
        .isEqualTo(expected.getBusinessHours());
  }

  private static final String json =
      """
      {
      	"id": 3,
      	"type": "BUSINESS",
      	"username": "Shreeji Newsagents",
      	"firstName": "Shreeji",
      	"lastName": "Newsagents",
      	"email": "shreejinews@btopenworld.com",
      	"avatarUrl": "https://www.shreejinewsagents.com/cdn/shop/files/shreeji-logo_400x.png",
      	"birthday": "2024-01-11T02:16:46.915875Z",
      	"businessName": "Shreeji Newsagents",
      	"coverUrls": ["https://www.shreejinewsagents.com/cdn/shop/files/6_3024x.jpg"],
      	"homepageUrl": "https://www.shreejinewsagents.com/",
      	"category": "Newsagent",
      	"introduction": "We offer a reliable delivery service for specific titles, newspapers or magazines.",
      	"phone": "+442079355055",
      	"businessHours": [{
      		"id": null,
      		"dayOfWeek": "Monday",
      		"closed": false,
      		"openingTime": [{
      			"hour": 9,
      			"minute": 0
      		}, {
            "hour": 18,
            "minute": 0
      		}],
      		"closingTime": [{
      			"hour": 16,
      			"minute": 0
      		}, {
            "hour": 21,
            "minute": 30
      		}],
      		"user": null
      	}, {
      		"id": null,
      		"dayOfWeek": "Tuesday",
      		"closed": true,
      		"openingTime": null,
      		"closingTime": null,
      		"user": null
      	}, {
      		"id": null,
      		"dayOfWeek": "Wednesday",
      		"closed": false,
      		"openingTime": [{
      			"hour": 8,
      			"minute": 0
      		}, {
            "hour": 17,
            "minute": 0
      		}],
      		"closingTime": [{
      			"hour": 15,
      			"minute": 0
      		}, {
            "hour": 20,
            "minute": 30
      		}],
      		"user": null
      	}, {
      		"id": null,
      		"dayOfWeek": "Thursday",
      		"closed": false,
      		"openingTime": [{
      			"hour": 9,
      			"minute": 0
      		}, {
            "hour": 18,
            "minute": 0
      		}],
      		"closingTime": [{
      			"hour": 16,
      			"minute": 0
      		}, {
            "hour": 21,
            "minute": 30
      		}],
      		"user": null
      	}, {
      		"id": null,
      		"dayOfWeek": "Friday",
      		"closed": false,
      		"openingTime": [{
      			"hour": 9,
      			"minute": 0
      		}, {
            "hour": 18,
            "minute": 0
      		}],
      		"closingTime": [{
      			"hour": 16,
      			"minute": 0
      		}, {
            "hour": 21,
            "minute": 30
      		}],
      		"user": null
      	}, {
      		"id": null,
      		"dayOfWeek": "Saturday",
      		"closed": true,
      		"openingTime": null,
      		"closingTime": null,
      		"user": null
      	}, {
      		"id": null,
      		"dayOfWeek": "Sunday",
      		"closed": true,
      		"openingTime": null,
      		"closingTime": null,
      		"user": null
      	}],
      	"location": {
      		"latitude": 51.51871309884953,
      		"longitude": -0.15449968748875476
      	},
      	"address": "6 Chiltern St, London W1U 7PT, United Kingdom"
      }
      """;
}
