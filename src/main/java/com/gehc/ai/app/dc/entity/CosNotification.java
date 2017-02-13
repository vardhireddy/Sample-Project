package com.gehc.ai.app.dc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by 200014175 on 11/2/2016.
 */
@Entity
public class CosNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

     /**
     * Actual message from COS notification, can be parsed
     */
    private Object message;
    /**
     * @return the message
     */
    public Object getMessage() {
        return message;
    }
    /**
     * @param message the message to set
     */
    public void setMessage( Object message ) {
        this.message = message;
    }
   
}
