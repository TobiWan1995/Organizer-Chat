package com.example.projekt1.models.plugins;

<<<<<<< HEAD
import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt1.R;
import com.example.projekt1.activities.chat.ChatActivity;
import com.example.projekt1.models.plugins.pluginData.Poll;
import com.google.firebase.database.collection.LLRBNode;

public class PluginPoll extends Plugin<Poll> {

    /*  Abstimmung = Poll
        Abstimmungsoptionen = PollOptionen
    TODO:
    Wie eine Abstimmung worked einbauen
        Option für mehrfaches auswählen von PollOptionen
        Anzeigen der Ergebnisse in Prozent
        Poll zurücksetzen

    Firebase anbindung
    Layout und Design beenden
    PollAdapter Klasse speziell onBindViewHolder checken ob worked
    anbindung an bestehende App
    Poll zurücksetzen funktion^^^^
    PollOptionen bennenen und speichern um auf anderen Geräten anzuzeigen
    Ob PollOption ausgewählt wurde muss auch gespeichert werden




    Least Priority:
    Visuelle Darstellung der Ergebnisse
     */

    //public PluginPoll()
    RecyclerView recyclerView;
    PollAdapter pollAdapter;
    EditText pollTitle;
    CheckBox pollChecked;
    Button pollSubmitButton;


=======
import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.pluginData.Poll;

public class PluginPoll extends Plugin<Poll> {

>>>>>>> origin/master
    @Override
    public void doPluginStuff() {

    }
<<<<<<< HEAD







=======
>>>>>>> origin/master
}
