package com.medici.stack.model.ack;

/**
 * ***************************************
 *
 * @desc: ACK确认报文的Model
 *
 * ***************************************
 */
public class AckModel<T> {

    /**
     * 序列Id
     */
    private Long sequenceId;

    /**
     * 核心数据
     */
    private T content;

    public Long getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Long sequenceId) {
        this.sequenceId = sequenceId;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AckModel{" +
                "sequenceId=" + sequenceId +
                ", content=" + content +
                '}';
    }
}
