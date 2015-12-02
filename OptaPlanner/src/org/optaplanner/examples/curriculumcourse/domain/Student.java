package org.optaplanner.examples.curriculumcourse.domain;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.examples.common.domain.AbstractPersistable;

/**
 * Created by shpandha on 9/6/2015.
 */
@XStreamAlias("Student")
public class Student extends AbstractPersistable {
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String i) {
        this.code = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return code.toString();
    }

    @Override
    public String toString() {
        return code.toString();
    }
}
