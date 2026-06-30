package com.focusguard.app.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class BlockedDomainDao_Impl implements BlockedDomainDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BlockedDomain> __insertionAdapterOfBlockedDomain;

  private final EntityDeletionOrUpdateAdapter<BlockedDomain> __deletionAdapterOfBlockedDomain;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByDomain;

  public BlockedDomainDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBlockedDomain = new EntityInsertionAdapter<BlockedDomain>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `blocked_domains` (`domain`) VALUES (?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BlockedDomain entity) {
        if (entity.getDomain() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getDomain());
        }
      }
    };
    this.__deletionAdapterOfBlockedDomain = new EntityDeletionOrUpdateAdapter<BlockedDomain>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `blocked_domains` WHERE `domain` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BlockedDomain entity) {
        if (entity.getDomain() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getDomain());
        }
      }
    };
    this.__preparedStmtOfDeleteByDomain = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM blocked_domains WHERE domain = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final BlockedDomain domain, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBlockedDomain.insert(domain);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object delete(final BlockedDomain domain, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBlockedDomain.handle(domain);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteByDomain(final String domain, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByDomain.acquire();
        int _argIndex = 1;
        if (domain == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, domain);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteByDomain.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Flow<List<BlockedDomain>> getAll() {
    final String _sql = "SELECT * FROM blocked_domains ORDER BY domain ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"blocked_domains"}, new Callable<List<BlockedDomain>>() {
      @Override
      @NonNull
      public List<BlockedDomain> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDomain = CursorUtil.getColumnIndexOrThrow(_cursor, "domain");
          final List<BlockedDomain> _result = new ArrayList<BlockedDomain>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BlockedDomain _item;
            final String _tmpDomain;
            if (_cursor.isNull(_cursorIndexOfDomain)) {
              _tmpDomain = null;
            } else {
              _tmpDomain = _cursor.getString(_cursorIndexOfDomain);
            }
            _item = new BlockedDomain(_tmpDomain);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<String>> getDomains() {
    final String _sql = "SELECT domain FROM blocked_domains";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"blocked_domains"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getString(0);
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
