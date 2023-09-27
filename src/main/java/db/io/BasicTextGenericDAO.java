package db.io;

import db.GenericDAO;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicTextGenericDAO<T> implements IBasicTextIO<T>, GenericDAO<T> {

    protected List<String> lines;

    protected List<String> copyLines;

    protected BasicTextIO<String> strIO;

    public BasicTextGenericDAO(String fileName) {
        strIO = new BasicTextIO<>(fileName) {
            @Override
            public boolean isMalformed(String line) {
                return false;
            }

            @Override
            public String build(String line) {
                return line;
            }

            @Override
            public String write(String o) {
                return o;
            }
        };
        initialize();
    }

    public void initialize() {
        lines = strIO.read();
        saveState();
    }

    public void rollback() {
        lines = new ArrayList<>(copyLines);
    }

    public boolean prepareToCommit() {
        saveState();
        return true;
    }

    public boolean commit() {
        saveState();
        return strIO.save(lines);
    }

    protected void saveState() {
        copyLines = new ArrayList<>(lines);
    }

    protected T buildFromString(String line) {
        if (!isMalformed(line)) {
            T o = build(line);
            if (o != null) {
                return o;
            } else {
                System.out.printf("\"%s\" can not be built\n", line);
            }
        } else {
            System.out.printf("Line: \"%s\" is malformed\n", line);
        }
        return null;
    }

    @Override
    public boolean insert(T something) {
        if (something == null) {
            return false;
        }
        String line = write(something);
        if (line != null) {
            lines.add(line);
        }
        return true;
    }

    @Override
    public T read(int id) {
        return buildFromString(lines.get(id));
    }

    @Override
    public List<T> readAll() {
        List<T> list = new ArrayList<>();
        for (var line : lines) {
            T o = buildFromString(line);
            if (o != null) {
                list.add(o);
            }
        }
        return list;
    }

    @Override
    public boolean update(T something) {
        return insert(something);
    }

    @Override
    public boolean delete(int id) {
        if (id >= 0 && id < lines.size()) {
            String removedLine = lines.remove(id);
            return removedLine != null;
        }
        return false;
    }

    public boolean update(int id, T something) {
        if (id >= 0 && id < lines.size()) {
            lines.set(id, write(something));
            return true;
        }
        return false;
    }

    // Getters

    public List<String> getLines() {
        return lines;
    }

    public List<String> getCopyLines() {
        return copyLines;
    }

    public BasicTextIO<String> getStrIO() {
        return strIO;
    }

    public String getFileName() {
        return strIO.getFileName();
    }

    public void setFileName(String fileName) {
        strIO.setFileName(fileName);
    }

    public int size() {
        return lines.size();
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public int indexOf(T something) {
        return lines.indexOf(something);
    }

    @Override
    public String toString() {
        return "BasicTextGenericDAO{" +
                "fileName='" + strIO.fileName + '\'' +
                "}";
    }

}
