package com.noosphere.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HistoryMapperTest {

    private HistoryMapper historyMapper;

    @BeforeEach
    public void setUp() {
        historyMapper = new HistoryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(historyMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(historyMapper.fromId(null)).isNull();
    }
}
