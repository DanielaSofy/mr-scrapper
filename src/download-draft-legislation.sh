#!/bin/bash

wget --recursive --domains gaceta.diputados.gob.mx --no-parent --html-extension --convert-links --restrict-file-names=windows -R pdf,doc,jpg,gif http://gaceta.diputados.gob.mx/gp_iniciativas.html
