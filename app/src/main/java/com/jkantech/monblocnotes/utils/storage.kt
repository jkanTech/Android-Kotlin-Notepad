package com.jkantech.monblocnotes.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.jkantech.monblocnotes.Note
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*


private const val TAG = "storage"

fun persistNote(context: Context, note: Note) : Boolean {
    val saved = true

    if (TextUtils.isEmpty(note.filename)) {
        note.filename = UUID.randomUUID().toString() + ".note"
    }
    Log.i(TAG, "Saving note $note")
    val fileOutput = context.openFileOutput(note.filename, Context.MODE_PRIVATE)
    val outputStream = ObjectOutputStream(fileOutput)
    outputStream.writeObject(note)

    return saved
}

fun loadNotes(context: Context) : MutableList<Note> {
    val notes = mutableListOf<Note>()
    Log.i(TAG, "Loading notes...")

    val notesDir = context.filesDir
    for(filename in notesDir.list()) {
        val note = loadNote(context, filename)
        Log.i(TAG, "Loaded note $note")
        notes.add(note)
    }
    return notes
}



fun deleteNote(context: Context, note: Note): Boolean {
    return context.deleteFile(note.filename)
}

private fun loadNote(context: Context, filename: String) : Note {
    val fileInput = context.openFileInput(filename)
    val inputStream = ObjectInputStream(fileInput)
    val note = inputStream.readObject() as Note
    return note
}

