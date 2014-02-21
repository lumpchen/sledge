/*
 * Copyright 2008 Pirion Systems Pty Ltd, 139 Warry St,
 * Fortitude Valley, Queensland, Australia
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package me.lumpchen.sledge.pdf.syntax.decrypt;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;

/**
 * Produces a {@link PDFDecrypter} for documents given a (possibly non-existent)
 * Encrypt dictionary. Supports decryption of versions 1, 2 and 4 of the
 * password-based encryption mechanisms as described in PDF Reference version
 * 1.7. This means that it supports RC4 and AES encryption with keys of 40-128
 * bits; esentially, password-protected documents with compatibility up to
 * Acrobat 8.
 * 
 * @See "PDF Reference version 1.7, section 3.5: Encryption"
 * @author Luke Kirby
 */
public class PDFDecrypterFactory {

	public static PDFDecrypter createDecrypter(IndirectObject encryptObj,
			PArray doucmentID, PDFPassword pwd)
			throws PDFAuthenticationFailureException {
		return null;
	}
}
