package com.rh.heji.data.repository

import com.rh.heji.data.DataRepository
import com.rh.heji.data.db.Book
import com.rh.heji.data.db.STATUS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BookRepository : DataRepository() {
    suspend fun addBook(book: Book) {
        bookDao.insert(book)
//        network.bookUpdate(book.id, book.name, book.type!!).apply {
//            if (code == OK) {
//                book.syncStatus = STATUS.SYNCED
//                bookDao.update(book)
//            }
//        }
    }

    suspend fun deleteBook(book_id: String) {
        bookDao.preDelete(book_id)
//        network.bookDelete(book_id).apply {
//            if (code == OK) {
//                bookDao.deleteById(book_id)
//            }
//        }
    }

    suspend fun updateBook(book: Book) {
        book.syncStatus = STATUS.UPDATED
        bookDao.update(book)
//        network.bookUpdate(book.id, book.name, book.type!!).apply {
//            if (code == OK) {
//                book.syncStatus = STATUS.SYNCED
//                bookDao.update(book)
//            }
//        }
    }

    suspend fun queryBook(): Flow<MutableList<Book>> {
        return bookDao.allBooks()
    }
}