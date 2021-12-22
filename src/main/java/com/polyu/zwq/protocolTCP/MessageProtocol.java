package com.polyu.zwq.protocolTCP;

import lombok.Data;

@Data
public class MessageProtocol {
    private int length;
    private byte[] content;
}
