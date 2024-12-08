package com.example.cine.controlador

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.cine.modelo.Horario
import com.example.cine.modelo.Pelicula
import com.example.cine.modelo.User

class PeliculaDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "peliculas.db"
        private const val DATABASE_VERSION =
           4   // Incrementamos la versión para manejar cambios en la base de datos
        const val TABLE_NAME = "peliculas"
        const val TABLE_HORARIOS = "horarios"
        const val TABLE_USUARIOS = "usuarios"  // Nueva tabla para los usuarios
        const val COLUMN_ID = "id"
        const val COLUMN_TITULO = "titulo"
        const val COLUMN_DESCRIPCION = "descripcion"
        const val COLUMN_IMAGEN = "imagen"
        const val COLUMN_TRES_D = "tresD"
        const val COLUMN_RESTRICCION_EDAD = "restriccionEdad"
        const val COLUMN_HORA_INICIO = "horaInicio"
        const val COLUMN_HORA_FIN = "horaFin"
        const val COLUMN_PELICULA_ID = "pelicula_id"
        const val COLUMN_CORREO = "correo"  // Correo del usuario
        const val COLUMN_CONTRASENA = "contrasena"  // Contraseña del usuario
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITULO TEXT NOT NULL,
                $COLUMN_DESCRIPCION TEXT NOT NULL,
                $COLUMN_IMAGEN TEXT NOT NULL,
                $COLUMN_TRES_D INTEGER NOT NULL,
                $COLUMN_RESTRICCION_EDAD TEXT NOT NULL
            )
        """
        db.execSQL(createTableQuery)

        // Crear la tabla de horarios
        val createHorariosTableQuery = """
            CREATE TABLE $TABLE_HORARIOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_HORA_INICIO TEXT NOT NULL,
                $COLUMN_HORA_FIN TEXT NOT NULL,
                $COLUMN_PELICULA_ID INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_PELICULA_ID) REFERENCES $TABLE_NAME($COLUMN_ID)
            )
        """
        db.execSQL(createHorariosTableQuery)

        // Crear la tabla de usuarios
        val createUsuariosTableQuery = """
            CREATE TABLE $TABLE_USUARIOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CORREO TEXT NOT NULL UNIQUE,
                $COLUMN_CONTRASENA TEXT NOT NULL
            )
        """
        db.execSQL(createUsuariosTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HORARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Método para insertar un nuevo usuario
    fun insertUser(user: User): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CORREO, user.correo)
            put(COLUMN_CONTRASENA, user.constrasena)
        }
        return db.insert(TABLE_USUARIOS, null, values)
    }

    // Método para verificar si el usuario y la contraseña son correctos
    fun validateUser(correo: String, contrasena: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            "$COLUMN_CORREO = ? AND $COLUMN_CONTRASENA = ?",
            arrayOf(correo, contrasena),
            null,
            null,
            null
        )
        val isValid = cursor.moveToFirst()  // Si el cursor tiene un resultado, el usuario es válido
        cursor.close()
        return isValid
    }

    // Método para insertar una película
    fun insertPelicula(pelicula: Pelicula): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITULO, pelicula.tituloPelicula)
            put(COLUMN_DESCRIPCION, pelicula.descripcionPelicula)
            put(COLUMN_IMAGEN, pelicula.imagenPelicula)
            put(COLUMN_TRES_D, if (pelicula.tresD) 1 else 0)
            put(COLUMN_RESTRICCION_EDAD, pelicula.restriccionDeEdad)
        }
        val peliculaId = db.insert(TABLE_NAME, null, values)

        // Insertar horarios de la película
        pelicula.horario.forEach { horario ->
            val horarioValues = ContentValues().apply {
                put(COLUMN_HORA_INICIO, horario.horaInicio)
                put(COLUMN_HORA_FIN, horario.horaFin)
                put(COLUMN_PELICULA_ID, peliculaId)
            }
            db.insert(TABLE_HORARIOS, null, horarioValues)
        }

        return peliculaId
    }

    // Método para obtener todas las películas con sus horarios
    fun getPeliculas(): ArrayList<Pelicula> {
        val db = readableDatabase
        val peliculas = ArrayList<Pelicula>()
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION))
            val imagen = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGEN))
            val tresD = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TRES_D)) == 1
            val restriccionEdad =
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTRICCION_EDAD))

            val pelicula = Pelicula(
                titulo,
                descripcion,
                imagen,
                ArrayList(),
                tresD,
                restriccionEdad
            )

            // Obtener los horarios de esta película
            val horariosCursor = db.query(
                TABLE_HORARIOS,
                null,
                "$COLUMN_PELICULA_ID = ?",
                arrayOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))),
                null,
                null,
                null
            )

            while (horariosCursor.moveToNext()) {
                val horaInicio =
                    horariosCursor.getString(horariosCursor.getColumnIndexOrThrow(COLUMN_HORA_INICIO))
                val horaFin =
                    horariosCursor.getString(horariosCursor.getColumnIndexOrThrow(COLUMN_HORA_FIN))
                pelicula.horario.add(Horario(horaInicio, horaFin))
            }

            horariosCursor.close()
            peliculas.add(pelicula)
        }
        cursor.close()
        return peliculas
    }
}
