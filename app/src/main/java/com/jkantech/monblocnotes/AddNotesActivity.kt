package com.jkantech.monblocnotes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AddNotesActivity : AppCompatActivity() {

    companion object {
        val REQUEST_EDIT_NOTE = 1

        val EXTRA_NOTE = "note"
        val EXTRA_NOTE_INDEX = "noteIndex"

        val ACTION_SAVE = "com.jkantech.monblocnotes.actions.ACTION_SAVE"
        val ACTION_DELETE = "com.jkantech.monblocnotes.action.ACTION_DELETE"

    }

    lateinit var note: Note
    var noteIndex: Int = -1
    lateinit var titleView: TextView
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_notes)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitle("Ajouter une note")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        note = intent.getParcelableExtra<Note>(EXTRA_NOTE)
        noteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, -1)

        titleView = findViewById<TextView>(R.id.title)
        textView = findViewById<TextView>(R.id.text)

        titleView.text = note.title
        textView.text = note.text
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                saveNote()
                return true
            }



            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        saveNoteAuto()
       // super.onBackPressed()
    }




    fun saveNote() {
        note.title = titleView.text.toString()
        note.text = textView.text.toString()

        if (note.title!!.trim()=="" && note.text!!.trim()=="" ){
           // toast("Aucune note à sauvegarder")
           finish()

        }else {
            intent = Intent(ACTION_SAVE)
            intent.putExtra(EXTRA_NOTE, note as Parcelable)
            intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
            toast("Note sauvegardée")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
 fun saveNoteAuto() {
        note.title = titleView.text.toString()
        note.text = textView.text.toString()

        if (note.title!!.trim()=="" && note.text!!.trim()=="" ){
           // toast("Aucune note à sauvegarder")
           finish()

        }else {
            intent = Intent(ACTION_SAVE)
            intent.putExtra(EXTRA_NOTE, note as Parcelable)
            intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
            toast("Note sauvegardée automatiquemnt")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    fun deleteNote() {
        intent = Intent(ACTION_DELETE)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    fun Context.toast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}
