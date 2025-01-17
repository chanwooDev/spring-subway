package subway.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LineSections 단위 테스트")
class LineSectionsTest {

    Line lineA;
    Line lineB;
    Station stationA;
    Station stationB;
    Station stationC;
    Station stationD;

    @BeforeEach
    void setUp() {
        lineA = new Line(1L, "A", "red");
        lineB = new Line(2L, "B", "blue");
        stationA = new Station(1L, "A");
        stationB = new Station(2L, "B");
        stationC = new Station(3L, "C");
        stationD = new Station(4L, "D");
    }

    @Test
    @DisplayName("노선을 생성할 때 한 구간을 생성한다")
    void createLineWithOneSection() {
        Section section = new Section(lineA, stationA, stationB, 3);

        assertThatCode(() -> new LineSections(lineA, section))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("다른 노선에 속한 구간들로 생성할 수 없습니다.")
    void cannotCreateWithSectionsOfOtherLines() {
        Line otherLine = new Line(2L, "lineB", "red");
        Section sectionA = new Section(lineA, stationA, stationB, 3);
        Section sectionB = new Section(otherLine, stationB, stationC, 3);

        assertThatThrownBy(() -> new LineSections(lineA, new Sections(List.of(sectionA, sectionB))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("다른 노선에 속한 구간은 추가할 수 없습니다.")
    void cannotAddSectionToLine() {
        Section sectionA = new Section(lineA, stationA, stationB, 3);
        Section sectionB = new Section(lineA, stationB, stationC, 3);
        Section sectionOfOtherLine = new Section(lineB, stationC, stationD, 3);

        LineSections lineSections = new LineSections(lineA, new Sections(List.of(sectionA, sectionB)));

        assertThatThrownBy(() -> lineSections.add(sectionOfOtherLine))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
