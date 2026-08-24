import Testing
import SerdeWith

@Suite("SerdeWith Swift Export Tests")
struct SerdeWithExportTests {
    @Test("Swift module loads and verifies basic constants and operations")
    func testModuleExports() {
        #expect(SerdeWithLib.shared.VERSION == "3.17.0")
        #expect(SpaceSeparator.shared.separator() == " ")
        #expect(CommaSeparator.shared.separator() == ",")
    }
}

