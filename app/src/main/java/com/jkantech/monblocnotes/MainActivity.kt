package com.jkantech.monblocnotes

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.jkantech.monblocnotes.utils.loadNotes
import com.jkantech.monblocnotes.utils.persistNote


class MainActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var adapter: NoteAdapter
    lateinit var notes: MutableList<Note>
    // lateinit var contextMenu:Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notes = loadNotes(this)

        adapter = NoteAdapter(notes, this)



        if (notes.isNotEmpty()) {
            loadNotes(this)
            notes.remove(Note("Ma note", "Ceci est ma premiere note!"))


        } else {
            notes.add(Note("Ma note", "Ceci est ma premiere note!"))


        }


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinator_layout)
        // val addnote findViewById(R.id.create_note_fab).setOnClickListener(this)

        val fab = findViewById<FloatingActionButton>(R.id.create_note_fab)
        fab.setOnClickListener {
            AddNote()
        }





        val recyclerView = findViewById<RecyclerView>(R.id.notes_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        when (requestCode) {
            NoteDetailActivity.REQUEST_EDIT_NOTE -> processEditNoteResult(data)
        }
    }
    override fun onClick(view: View) {
        if (view.tag != null) {
         // startAddNotesActivity(view.tag as Int)
            startEditNotesActivity(view.tag as Int)
        }else{
            when (view.id) {
                R.id.create_note_fab -> AddNote()
            }

        }



    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        //val search = menu.findItem(R.id.action_search)

         //val search=menu.findItem(R.id.action_search)
      //  val searchView = MenuItemCompat.getActionView(search) as SearchView
     //   searchView.queryHint="Recherche..."
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_about->{
                val inflater = LayoutInflater.from(this)
                val subView = inflater.inflate(R.layout.suppression_dialogue1, null)
                AlertDialog.Builder(this)
                    .setTitle("A propos")
                   .setMessage("Développée par jkanTech")

                    .setPositiveButton("Fermer",DialogInterface.OnClickListener{
                        dialog: DialogInterface?, i: Int ->
                        dialogue("Fermer")
                    })





                    .show()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun processEditNoteResult(data: Intent) {
        val noteIndex = data.getIntExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, -1)

        when(data.action) {
            NoteDetailActivity.ACTION_SAVE -> {
                val note = data.getParcelableExtra<Note>(NoteDetailActivity.EXTRA_NOTE)
                saveNote(note, noteIndex)
            }
            NoteDetailActivity.ACTION_DELETE -> deleteNote(noteIndex)
        }
    }

    fun saveNote(note: Note, noteIndex: Int) {
        persistNote(this, note)
        if (noteIndex < 0) {
            notes.add(0, note)
        } else {
            notes[noteIndex] = note
        }
        adapter.notifyDataSetChanged()
    }

    fun deleteNote(noteIndex: Int) {
        if (noteIndex < 0) {
            return
        }
        val note = notes.removeAt(noteIndex)
        com.jkantech.monblocnotes.utils.deleteNote(this, note)
        adapter.notifyDataSetChanged()

        Snackbar.make(coordinatorLayout, "${note.title} supprimé", Snackbar.LENGTH_SHORT)
            .show()
    }



    /*
    *
    * Ajout de la note
     */

    fun AddNote() {
        startAddNotesActivity(-1)
    }

    fun startAddNotesActivity(noteIndex: Int) {
        val note = if (noteIndex < 0) Note() else notes[noteIndex]

        val intent = Intent(this, AddNotesActivity::class.java)
        intent.putExtra(AddNotesActivity.EXTRA_NOTE, note as Parcelable)
        intent.putExtra(AddNotesActivity.EXTRA_NOTE_INDEX, noteIndex)
        startActivityForResult(intent, AddNotesActivity.REQUEST_EDIT_NOTE)
    }
    /*
    *
    *  Edition de la Note
    *
     */
    fun EditNote() {
        startEditNotesActivity(-1)
    }

    fun startEditNotesActivity(noteIndex: Int) {
        val note = if (noteIndex < 0) Note() else notes[noteIndex]

        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE, note as Parcelable)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, noteIndex)
        startActivityForResult(intent, NoteDetailActivity.REQUEST_EDIT_NOTE)
    }
    private fun recherche(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
               // if (adapter != null) adapter!!.filter.filter(newText)
                return true
            }
        })
    }
fun Context.dialogue(message:String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}
}
