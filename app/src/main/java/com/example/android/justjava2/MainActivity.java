package com.example.android.justjava2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;
    String priceMessage;
    String nameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hide the keyboard during the start of the app
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {

        //hide the keyboard after pressing the button
        hideKeyBoard(view);

        if (quantity == 100) {
            //Show an error message as a toast
            Toast.makeText(this, getString(R.string.toast_more_than_onehundred), LENGTH_SHORT).show();
            //Exit this method early because of the error
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {

        //hide the keyboard after pressing the button
        hideKeyBoard(view);

        if (quantity == 1) {
            //Show an error message as a toast
            Toast.makeText(this, getString(R.string.toast_less_than_one), LENGTH_SHORT).show();
            //Exit this method early because of the error
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //Allows user to enter their name
        EditText nameEditText = findViewById(R.id.name_edit_text);
        String name = nameEditText.getText().toString();

        //Figures out if user wants Whipped Cream topping
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        //Figures out if user wants Chocolate topping
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        //Figures out if user wants Caramel topping
        CheckBox caramelCheckBox = findViewById(R.id.caramel_checkbox);
        boolean hasCaramel = caramelCheckBox.isChecked();

        //Figures out if user wants Cinnamon topping
        CheckBox cinnamonCheckBox = findViewById(R.id.cinnamon_checkbox);
        boolean hasCinnamon = cinnamonCheckBox.isChecked();

        //Calculates the price of order
        int price = calculatePrice(hasWhippedCream, hasChocolate, hasCaramel, hasCinnamon);
        //Displays the price and order summary
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, hasCaramel, hasCinnamon, name);


        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Creates a summary of the order.
     *
     * @param price           of the order
     * @param addWhippedCream is whether or not the user wants whipped cream
     * @param addChocolate    is whether or not the user wants chocolate
     * @param addCaramel      is whether or not the user wants caramel
     * @param addCinnamon     is whether or not the user wants cinnamon
     * @param nameEditText    is the user's name
     * @return text summary: the name, quantity of cups of coffee, total price and Thank you.
     */
    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate, boolean addCaramel, boolean addCinnamon, String nameEditText) {
        String priceMessage = getString(R.string.order_summary_name, nameEditText);
        priceMessage += "\n" + getString(R.string.order_summary_whipped_cream, addWhippedCream);
        priceMessage += "\n" + getString(R.string.order_summary_chocolate, addChocolate);
        priceMessage += "\n" + getString(R.string.order_summary_caramel, addCaramel);
        priceMessage += "\n" + getString(R.string.order_summary_cinnamon, addCinnamon);
        priceMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
        priceMessage += "\n" + getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * Calculates the price of the order.
     *
     * @param hasWhippedCream is whether or not the user wants whipped cream
     * @param hasChocolate    is whether or not the user wants chocolate
     * @param hasCaramel      is whether or not the user wants caramel
     * @param hasCinnamon     is whether or not the user wants cinnamon     *
     * @return total price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate, boolean hasCaramel, boolean hasCinnamon) {
        //Price of one cup of coffee
        int basePrice = 5;

        //Add $1 if the user wants whipped cream topping
        if (hasWhippedCream) {
            basePrice = basePrice + 1;
        }

        //Add $2 if the user wants chocolate topping
        if (hasChocolate) {
            basePrice = basePrice + 2;
        }

        //Add $1 if the user wants caramel topping
        if (hasCaramel) {
            basePrice = basePrice + 1;
        }

        //Add $1 if the user wants cinnamon topping
        if (hasCinnamon) {
            basePrice = basePrice + 1;
        }

        //Calculates the total order price by multiplying the quantity by the base price
        return quantity * basePrice;
    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }

    //this method hide the keyboard after pressing a button
    public void hideKeyBoard(View view) {
        View exView = this.getCurrentFocus();
        if (exView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // This method saves the data to keep it from resetting to initial values on rotation of orientation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Quantity", quantity);
        outState.putString("PriceMessage", priceMessage);
        outState.putString("Name", nameEditText);
    }

    // This method restores the data to keep it from resetting to initial values on rotation of orientation
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        quantity = savedInstanceState.getInt("Quantity");
        priceMessage = savedInstanceState.getString("PriceMessage");
        nameEditText = savedInstanceState.getString("Name");
        displayQuantity(quantity);
    }
}
