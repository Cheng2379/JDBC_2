package bean;

public class Student {

    private int flowID;//流水号
    private int type;//考试等级类型
    private String IDCard;//身份证号
    private String ExamCard;//准考证号
    private String  studentName;//学生姓名
    private String location;//区域
    private int grade;//成绩

    public Student() {
        super();
    }

    public Student(int flowID, int type, String IDCard, String examCard, String studentName, String location, int grade) {
        this.flowID = flowID;
        this.type = type;
        this.IDCard = IDCard;
        ExamCard = examCard;
        this.studentName = studentName;
        this.location = location;
        this.grade = grade;
    }

    public int getFlowID() {
        return flowID;
    }

    public void setFlowID(int flowID) {
        this.flowID = flowID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getExamCard() {
        return ExamCard;
    }

    public void setExamCard(String examCard) {
        ExamCard = examCard;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        System.out.print("========查询结果========");
        return "\n流水号: " + flowID +
                "\n四级/六级: " + type +
                "\n身份证号: " + IDCard +
                "\n准考证号: " + ExamCard +
                "\n学生姓名: " + studentName +
                "\n区域: " + location +
                "成绩: " + grade;
    }
}
