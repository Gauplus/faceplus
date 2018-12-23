package edu.njust.cn.faceplus;

public class User {
    private String name;
    private String nickname;
    private String birth;
    private String studentId;
    private String school;
    private String sex;
    private String identity;

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSchool() {
        return school;
    }

    public String getBirth() {
        return birth;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getSex() {
        return sex;
    }

    public String getIdentity() {
        return identity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public  User(String name, String nickname, String school, String birth, String sex, String studentId,String identity)
    {
        this.name=name;
        this.nickname=nickname;
        this.school=school;
        this.sex=sex;
        this.birth=birth;
        this.studentId=studentId;
        this.identity=identity;
    }


    @Override
    public String toString() {
        return this.name+","+this.nickname+","+this.birth+","+this.sex+","+this.school+","+studentId+","+this.identity;
    }
}
