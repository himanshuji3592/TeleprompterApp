package com.teleprompter.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.teleprompter.app.models.Script;
import java.util.List;

@Dao
public interface ScriptDao {

    @Insert
    long insertScript(Script script);

    @Update
    void updateScript(Script script);

    @Delete
    void deleteScript(Script script);

    @Query("SELECT * FROM scripts ORDER BY lastModifiedTime DESC")
    List<Script> getAllScripts();

    @Query("SELECT * FROM scripts WHERE id = :scriptId")
    Script getScriptById(int scriptId);

    @Query("SELECT * FROM scripts WHERE folder = :folder ORDER BY lastModifiedTime DESC")
    List<Script> getScriptsByFolder(String folder);

    @Query("SELECT * FROM scripts WHERE title LIKE '%' || :searchQuery || '%'")
    List<Script> searchScripts(String searchQuery);

    @Query("SELECT DISTINCT folder FROM scripts WHERE folder IS NOT NULL")
    List<String> getAllFolders();

    @Query("DELETE FROM scripts WHERE id = :scriptId")
    void deleteScriptById(int scriptId);
}
