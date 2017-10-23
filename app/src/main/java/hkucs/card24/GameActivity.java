package hkucs.card24;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.singularsys.jep.EvaluationException;
import com.singularsys.jep.Jep;
import com.singularsys.jep.ParseException;
import com.singularsys.jep.functions.Random;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    int target = 24;
    ImageButton[] cards;

    Button rePick;
    Button checkInput;
    Button clear;
    Button left;
    Button right;
    Button plus;
    Button minus;
    Button multiply;
    Button divide;
    TextView expression;

    int[] data = new int[]{0, 0, 0, 0};
    int[] card = new int[]{0, 0, 0, 0};
    int[] imageCount = new int[]{0, 0, 0, 0};

    int max = 52;
    int min = 1;

    private boolean prevContains(ArrayList<Integer> prev, int num) {
        for (int i = 0; i < prev.size(); i++) {
            if(prev.get(i) == num) {
                return true;
            }
        }

        return false;
    }

    private int generateRandomNumber(ArrayList<Integer> prev) {
        java.util.Random r = new java.util.Random();
        int num;

        num = r.nextInt(max - min + 1) + min;

        while(prevContains(prev, num)) {
            num = r.nextInt(max - min + 1) + min;
        }

        return num;
    }

    private void pickCard() {
        data = new int[4];
        ArrayList<Integer> prev = new ArrayList<Integer>();

        for (int i = 0; i < 4; i++) {
            card[i] = generateRandomNumber(prev);
            data[i] = ((int) card[i]) % 13 == 0 ? 13 : (int) card[i] % 13;
            prev.add(card[i]);
        }

        setClear();
    }

    private void setClear() {
        int resID;
        expression.setText("");

        for (int i = 0; i < 4; i++) {
            imageCount[i] = 0;
            resID = getResources().getIdentifier("card" + card[i], "drawable", "hkucs.card24");
            cards[i].setImageResource(resID);
            cards[i].setClickable(true);
        }
    }

    private void clickCard(int i) {
        int resId;
        String num;
        Integer value;

        if (expression.getText().toString().length() == 0 || !Character.isDigit(expression.getText().toString().charAt(expression.getText().toString().length() - 1))) {
            if (imageCount[i] == 0) {
                resId = getResources().getIdentifier("back_0", "drawable", "hkucs.card24");
                cards[i].setImageResource(resId);
                cards[i].setClickable(false);
                value = data[i];
                num = value.toString();

                expression.append(num);
                imageCount[i]++;
            }
        } else {
            Toast.makeText(GameActivity.this,
                    "Invalid usage of card",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void initCardImage() {
        for(int i = 0; i < 4; i++) {
            int resID = getResources().getIdentifier("back_0", "drawable", "hkucs.card24");
            cards[i].setImageResource(resID);
        }
    }

    private boolean checkInput(String input) {
        Jep jep = new Jep();
        Object res;

        try {
            jep.parse(input);
            res = jep.evaluate();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(GameActivity.this,
                    "Wrong Expression",
                    Toast.LENGTH_SHORT).show();
            return false;
        } catch (EvaluationException e) {
            e.printStackTrace();
            Toast.makeText(GameActivity.this,
                    "Wrong Expression",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        Double ca = (Double) res;
        if (Math.abs(ca - target) < 1e-6) {
            return true;
        }

        return false;
    }

    private void handleArithCloseBracketValid(String str) {
        String exp = expression.getText().toString();

        if (exp.endsWith(")")
                || (exp.length() > 0 ? Character.isDigit(exp.charAt(exp.length() - 1)) : false)) {
            expression.append(str);
        } else {
            Toast.makeText(GameActivity.this,
                    "Invalid usage of `" + str + "`",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        target = Integer.parseInt(i.getStringExtra("number"));

        rePick = (Button) findViewById(R.id.repick);
        checkInput = (Button) findViewById(R.id.equal);
        clear = (Button) findViewById(R.id.clear);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        plus = (Button) findViewById(R.id.plus);
        minus = (Button) findViewById(R.id.minus);
        multiply = (Button) findViewById(R.id.multiply);
        divide = (Button) findViewById(R.id.divide);
        expression = (TextView) findViewById(R.id.input);

        expression.setHint("Input expression such that the result is " + target);
        cards = new ImageButton[4];

        cards[0] = (ImageButton) findViewById(R.id.card1);
        cards[1] = (ImageButton) findViewById(R.id.card2);
        cards[2] = (ImageButton) findViewById(R.id.card3);
        cards[3] = (ImageButton) findViewById(R.id.card4);

        // initCardImage();
        pickCard();

        cards[0].setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                clickCard(0);
            }
        });

        cards[1].setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                clickCard(1);
            }
        });

        cards[2].setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                clickCard(2);
            }
        });

        cards[3].setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                clickCard(3);
            }
        });

        left.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                if (expression.getText().toString().length() == 0
                        || expression.getText().toString().endsWith("(")
                        || expression.getText().toString().endsWith("+")
                        || expression.getText().toString().endsWith("-")
                        || expression.getText().toString().endsWith("*")
                        || expression.getText().toString().endsWith("/")) {
                    expression.append("(");
                } else {
                    Toast.makeText(GameActivity.this,
                            "Invalid usage of `(`",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        right.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                handleArithCloseBracketValid(")");
            }
        });

        plus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                handleArithCloseBracketValid("+");
            }
        });

        minus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                handleArithCloseBracketValid("-");
            }
        });

        multiply.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                handleArithCloseBracketValid("*");
            }
        });

        divide.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                handleArithCloseBracketValid("/");
            }
        });

        clear.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                setClear();
            }
        });

        checkInput.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                String inputStr = expression.getText().toString();
                if (imageCount[0] * imageCount[1] * imageCount[2] * imageCount[3] == 0) {
                    Toast.makeText(GameActivity.this,
                            "All cards should be used",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (checkInput(inputStr)) {
                        Toast.makeText(GameActivity.this,
                                "Correct Answer",
                                Toast.LENGTH_SHORT).show();
                        pickCard();
                    } else {
                        Toast.makeText(GameActivity.this,
                                "Wrong Answer",
                                Toast.LENGTH_SHORT).show();
                        setClear();
                    }
                }
            }
        });
    }
}
