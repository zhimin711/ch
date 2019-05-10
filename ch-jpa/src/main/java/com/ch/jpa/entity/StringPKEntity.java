package com.ch.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 描述：com.zh.jdbc.entity
 *
 * @author 80002023
 *         2017/2/14.
 * @version 1.0
 * @since 1.8
 */
@MappedSuperclass
public class StringPKEntity extends BaseEntity {

    @Id
    @Column(name = "ID", length = 36, updatable = false)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id == null ? System.identityHashCode(this) : id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass().getPackage() != obj.getClass().getPackage()) {
            return false;
        }
        final LongPKEntity other = (LongPKEntity) obj;
        if (id == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!id.equals(other.getId())) {
            return false;
        }
        return true;
    }

}
