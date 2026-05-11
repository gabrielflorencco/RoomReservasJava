package com.roomreservas.application.port.in;

import com.roomreservas.application.dto.request.CriarUsuarioRequest;

import java.io.InputStream;
import java.util.List;

public interface UsuarioFileParser {

    List<CriarUsuarioRequest> parse(InputStream inputStream);
}