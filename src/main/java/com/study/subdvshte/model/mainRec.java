package com.study.subdvshte.model;

import java.math.BigInteger;

public record mainRec(
        BigInteger id,
        String arduino_board_type,
        String processor_type,
        String cnt,
        Float price,
        Float rate
) {
}
