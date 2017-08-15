package com.craftsharp;

import java.io.IOException;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

public interface SerializableEntity {
	public void read(ByteBufInputStream in) throws IOException;

	public void write(ByteBufOutputStream out) throws IOException;
}
