package com.medici.stack.model.ack;

/**
 * ***************************************
 *
 * @desc: ACK确认报文的Model
 * @author：李宗好
 * @time: 2018/1/16 0016 15:50
 * @email：lzh@cnbisoft.com
 * @version：
 * @history:
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
