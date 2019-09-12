all: body.docx body.pdf

FIGURES = images/uml-arkivstruktur-mappe.png

body.docx: $(FIGURES) body.md
	pandoc -t docx -f markdown -o body.docx body.md

body.pdf: $(FIGURES) body.md
	pandoc -f markdown -o body.pdf body.md

.puml.png:
	plantuml -p < $^ > $@.new && mv $@.new $@

clean:
	$(RM) body.pdf body.docx

.SUFFIXES: .md .pdf .docx .puml .png .svg
