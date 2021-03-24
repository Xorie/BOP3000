package application.bop3000.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "application/bop3000/faq")
public class FAQ implements Serializable {
    @ColumnInfo(name = "faqID")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int faqID;

    @ColumnInfo (name = "question")
    private String question;

    @ColumnInfo (name = "answer")
    private String answer;

    @NonNull
    public int getFaqID() {
        return faqID;
    }

    public void setFaqID(@NonNull int faqID) {
        this.faqID = faqID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String toString() {
        return "FAQ{" +
                "FaqID=" + faqID +
                ", Question=" + question +
                ", Answer=" + answer +
                '}';
    }
}
