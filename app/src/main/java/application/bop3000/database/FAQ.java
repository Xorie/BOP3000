package application.bop3000.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "FAQ")
public class FAQ implements Serializable {
    @ColumnInfo(name = "faqID")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int faqID;

    @ColumnInfo (name = "question")
    private String question;

    @ColumnInfo (name = "answer")
    private String answer;

    public FAQ(int faqID, String question, String answer){
        this.faqID = faqID;
        this.question = question;
        this.answer = answer;
    }

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
    public static FAQ[] populateFAQData() {
        return new FAQ[] {
                new FAQ(1, "Hvordan er det med frakt?", "Vi bruker Bring, og pakken vil komme i din postkasse eller med pose på døren (avhengig av hva du velger som frakt). Passer ikke boksen i din postkasse, vil du få en hentekode fra posten. Frakt er inkludert i alle våre abonnementer."),
                new FAQ(2, "Kommer det til å komme ekstra kostnader?", "Nei. Det vil ikke komme noen ekstra kostnader for deg når du kjøper din Knittersbox – med mindre du kjøper noen av våre andre produkter i tillegg."),
                new FAQ(3, "Når får jeg min første knittersbox?", "Din knitterbox vil dumpe ned i postkassen din ved månedsskifte hver måned. Dette er forutsatt at du har kjøpt boks før den 20. foregående måned.\n" +
                        "\n" +
                        "Eksempel: Din boks, kjøpt før den 20. september, vil du motta i månedsskiftet september/oktober. Din boks, kjøpt etter den 20. september, vil du motta i månedsskiftet oktober/november." ),
                new FAQ(4, "Hva slags bokser kan jeg velge mellom?", "Vi har to ulike abonnement; jord/farge & håndfarget.\n" +
                        "\n" +
                        "Farge/jord-abonnementet er vårt standard abonnement. Boksens standard-verdi er alltid minst 600,- hver måned.\n" +
                        "\n" +
                        "Håndfarget-abonnementet er helt likt medi-abonnementet, bare at det kun kommer håndfarget garn i denne boksen. Verdien på denne boksen er på minst 700,- hver måned."),
                new FAQ(5, "Hva inneholder boksen?", "Boksen inneholder ulike strikkerelaterte ting hver eneste måned. Strikketilbehøret kan være alt fra strikkefasthetsmålere og sakser til kjekke verktøy og spiselige godsaker du kan kombinere med strikkingen. Hele konseptet bygger rundt at dette skal være en overraskelse.\n" +
                        "\n" +
                        "I tillegg til dette kan boksen din inneholde garn fra alle mulige garnleverandører, i tillegg til vårt eget håndfargede garn.")
        };
    }
}
