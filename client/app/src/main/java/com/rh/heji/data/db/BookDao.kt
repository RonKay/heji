package com.rh.heji.data.db

import androidx.room.*

@Dao
interface BookDao {

    @Insert
    fun insert(book: Book): Long

    @Update(entity = Book::class, onConflict = OnConflictStrategy.REPLACE)
    fun update(book: Book): Int

    @Query("select count() from book  where name=:name")
    fun countByName(name: String): Int

    @Query("SELECT * FROM book ORDER BY id")
    fun allBooks(): MutableList<Book>

    @Query("select * from book where sync_status=:status")
    fun books(status: Int): MutableList<Book>

    @Transaction
    @Query("select * from book where name =:bookName")
    fun findBookWhitBills(bookName: String): MutableList<BookWithBills>

    @Query("select count(0) from book")
    fun count(): Int

    @Query("select count(0) from book WHERE id=:bookId")
    fun exist(bookId: String): Int

    @Query("select * from book where id =:id")
    fun findBook(id: String): MutableList<Book>

//    //~~~~~~~~~~~~~~~~~~~~~~~~book whit users~~~~~~~~~~~~~~~~~~~~~~~~~~//
//    @Transaction
//    @Query("select * from book")
//    fun booksWhitUsers(): MutableList<BookWhitUsers>

}
