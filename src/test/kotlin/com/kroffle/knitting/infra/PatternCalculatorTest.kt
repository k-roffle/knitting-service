package com.kroffle.knitting.infra

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.infra.design.PatternCalculator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PatternCalculatorTest {
    lateinit var calculator: PatternCalculator

    @BeforeEach
    fun setUp() {
        calculator = PatternCalculator()
    }

    @Test
    fun `게이지 계산이 잘 되어야 함`() {
        val target = Pattern(
            """
            {
               "blocks":[
                  {
                     "key":"8s16f",
                     "text":"1. 코잡기 61코",
                     "type":"unstyled",
                     "depth":0,
                     "inlineStyleRanges":[
                        {
                           "offset":7,
                           "length":3,
                           "style":"STITCH_CALCULATE_ROUND"
                        }
                     ],
                     "entityRanges":[],
                     "data":{}
                  },
                  {
                     "key":"5uumj",
                     "text":"Row1. 안 61코",
                     "type":"unstyled",
                     "depth":0,
                     "inlineStyleRanges":[
                         {
                           "offset":8,
                           "length":3,
                           "style":"STITCH_CALCULATE_ROUND_UP"
                        }
                     ],
                     "entityRanges":[],
                     "data":{}
                  },
                  {
                     "key":"bnalk",
                     "text":"Row2. (겉1, 늘림1)x59번, 겉2 (120코)",
                     "type":"unstyled",
                     "depth":0,
                     "inlineStyleRanges":[
                         {
                           "offset":16,
                           "length":3,
                           "style":"STITCH_REPEAT_CALCULATE_ROUND"
                        },
                        {
                           "offset":25,
                           "length":4,
                           "style":"STITCH_CALCULATE_ROUND"
                        }
                     ],
                     "entityRanges":[],
                     "data":{}
                  },
                  {
                     "key":"66bat",
                     "text":"Row3. 안 120코",
                     "type":"unstyled",
                     "depth":0,
                     "inlineStyleRanges":[
                        {
                           "offset":8,
                           "length":4,
                           "style":"STITCH_CALCULATE_ROUND"
                        }
                     ],
                     "entityRanges":[],
                     "data":{}
                  },
                  {
                     "key":"b6b24",
                     "text":"Row4. 겉1, 늘림1, (겉2, 늘림1) * 59번, 겉1 (180코)",
                     "type":"unstyled",
                     "depth":0,
                     "inlineStyleRanges":[
                        {
                           "offset":27,
                           "length":3,
                           "style":"STITCH_REPEAT_CALCULATE_ROUND"
                        },
                        {
                           "offset":36,
                           "length":4,
                           "style":"STITCH_CALCULATE_ROUND"
                        }
                     ],
                     "entityRanges":[],
                     "data":{}
                  },
                  {
                     "key":"9214a",
                     "text":"메리야스 뜨기로 27단을 계속해서 떠줍니다.",
                     "type":"unstyled",
                     "depth":0,
                     "inlineStyleRanges":[
                        {
                           "offset":9,
                           "length":3,
                           "style":"ROW_CALCULATE_ROUND_DOWN"
                        }
                     ],
                     "entityRanges":[],
                     "data":{}
                  }
               ],
               "entityMap":{}
            }
            """
        )
        val result: Pattern = calculator
            .calculateGauge(
                target,
                Gauge(15.0, 13.0),
                Gauge(18.0, 25.0),
            )
        val mapper = ObjectMapper().registerKotlinModule()
        assertThat(mapper.readTree(result.value)).isEqualTo(
            mapper.readTree(
                """
                {
                   "blocks":[
                      {
                         "key":"8s16f",
                         "text":"1. 코잡기 73코",
                         "type":"unstyled",
                         "depth":0,
                         "inlineStyleRanges":[
                            {
                               "offset":7,
                               "length":3,
                               "style":"STITCH_CALCULATE_ROUND"
                            }
                         ],
                         "entityRanges":[],
                         "data":{}
                      },
                      {
                         "key":"5uumj",
                         "text":"Row1. 안 74코",
                         "type":"unstyled",
                         "depth":0,
                         "inlineStyleRanges":[
                             {
                               "offset":8,
                               "length":3,
                               "style":"STITCH_CALCULATE_ROUND_UP"
                            }
                         ],
                         "entityRanges":[],
                         "data":{}
                      },
                      {
                         "key":"bnalk",
                         "text":"Row2. (겉1, 늘림1)x71번, 겉2 (144코)",
                         "type":"unstyled",
                         "depth":0,
                         "inlineStyleRanges":[
                             {
                               "offset":16,
                               "length":3,
                               "style":"STITCH_REPEAT_CALCULATE_ROUND"
                            },
                            {
                               "offset":25,
                               "length":4,
                               "style":"STITCH_CALCULATE_ROUND"
                            }
                         ],
                         "entityRanges":[],
                         "data":{}
                      },
                      {
                         "key":"66bat",
                         "text":"Row3. 안 144코",
                         "type":"unstyled",
                         "depth":0,
                         "inlineStyleRanges":[
                            {
                               "offset":8,
                               "length":4,
                               "style":"STITCH_CALCULATE_ROUND"
                            }
                         ],
                         "entityRanges":[],
                         "data":{}
                      },
                      {
                         "key":"b6b24",
                         "text":"Row4. 겉1, 늘림1, (겉2, 늘림1) * 71번, 겉1 (216코)",
                         "type":"unstyled",
                         "depth":0,
                         "inlineStyleRanges":[
                            {
                               "offset":27,
                               "length":3,
                               "style":"STITCH_REPEAT_CALCULATE_ROUND"
                            },
                            {
                               "offset":36,
                               "length":4,
                               "style":"STITCH_CALCULATE_ROUND"
                            }
                         ],
                         "entityRanges":[],
                         "data":{}
                      },
                      {
                         "key":"9214a",
                         "text":"메리야스 뜨기로 51단을 계속해서 떠줍니다.",
                         "type":"unstyled",
                         "depth":0,
                         "inlineStyleRanges":[
                            {
                               "offset":9,
                               "length":3,
                               "style":"ROW_CALCULATE_ROUND_DOWN"
                            }
                         ],
                         "entityRanges":[],
                         "data":{}
                      }
                   ],
                   "entityMap":{}
                }
                """
            )
        )
    }
}
