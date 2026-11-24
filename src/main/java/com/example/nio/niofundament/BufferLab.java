package com.example.nio.niofundament;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

class SharedBuffer {

    private String id;
    private ByteBuffer coreBuffer;

    public SharedBuffer(String id, int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be 0.");
        }
        this.id = id;
        this.coreBuffer = ByteBuffer.allocate(capacity);
    }

    public synchronized SharedBuffer putBytes(byte[] b) {
        if (b.length > coreBuffer.remaining()) {
            throw new IllegalStateException(
                    "Not enough space in %s: need %s, remainaing %s.".formatted(id, b.length,
                            this.coreBuffer.remaining()));
        }
        System.out.println("Allocating %s bytes to %s.".formatted(b.length, getId()));
        this.coreBuffer.put(b);
        return this;
    }

    public synchronized void switchRegime() {
        System.out.println("INFO: flipped %s.".formatted(getId()));
        this.coreBuffer.flip();
    }

    public synchronized SharedBuffer clearBuffer() {
        System.out.println("INFO: cleared %s.".formatted(getId()));
        this.coreBuffer.clear();
        return this;
    }

    public synchronized void getItems() {
        System.out.println(Arrays.toString(this.coreBuffer.array()));
    }

    public String getId() {
        return this.id;
    }

    public int getCapacity() {
        return this.coreBuffer.capacity();
    }

    public synchronized int getPosition() {
        return this.coreBuffer.position();
    }

    public synchronized int getLimit() {
        return this.coreBuffer.limit();
    }

    public ByteBuffer getCoreBuffer() {
        return this.coreBuffer;
    }

    @Override
    public String toString() {
        return "&SharedBuffer[id='%s', capacity=%s]".formatted(getId(), this.coreBuffer.capacity());
    }

}

public class BufferLab {

    private static final String CWD = new File("").getAbsolutePath();

    public static void readFileChannel(String src) {
        try (FileChannel channel = FileChannel.open(Path.of("%s/%s".formatted(CWD, src)))) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while ( channel.read(buffer) != -1 ) {
                buffer.flip();
                while(buffer.hasRemaining()) {
                    byte value = buffer.get();
                    System.out.println(value);
                }
                buffer.clear();
            }
        } catch (IOException e) {
            System.err.println("IOException: %s".formatted(e.getMessage()));
        }
    }

    public static void writeFileChannel(String dest, byte[] data) {
        try (FileChannel channel = FileChannel.open(
            Path.of(CWD).resolve(dest),
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE
        )) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (buffer.hasRemaining()) {
                buffer.put(data);
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            buffer.clear();
        } catch (IOException e) {
            System.err.println("IOException: %s".formatted(e.getMessage()));
        }
    }

    public static void main(String[] args) {

        // var shb = new SharedBuffer("shared-buffer-0", 10);
        // System.out.println(shb.toString());
        // shb.putBytes(new byte[] { (byte) 10, (byte) 20, (byte) 30, (byte) 40 });
        // shb.getItems();
        // System.out.println(shb.getPosition());
        // shb.clearBuffer();
        // System.out.println(shb.getPosition());

        writeFileChannel("example.txt", new byte[]{10, 20, 30});

    }

}
