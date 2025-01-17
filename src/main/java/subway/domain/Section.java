package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Section {

    private Long id;
    private final Line line;
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(Long id, Line line, Station upStation, Station downStation, int distance) {
        validatePositive(distance);
        validateStationsNotEqual(upStation, downStation);

        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this(null, line, upStation, downStation, distance);
    }

    public Section(Line line, Section section) {
        this(section.getId(), line, section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    private void validatePositive(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("구간 길이는 양수여야합니다 distance: \"" + distance + "\"");
        }
    }

    private void validateStationsNotEqual(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException(
                "구간의 상행역과 하행역은 같을 수 없습니다. upStation: " + upStation + ", downStation: "
                    + downStation);
        }
    }

    public boolean canPrecede(Section other) {
        return downStation.equals(other.upStation);
    }

    public boolean containsStation(Station station) {
        return this.upStation.equals(station) || this.downStation.equals(
            station);
    }

    public boolean hasSameUpStationOrDownStation(Section section) {
        return this.upStation.equals(section.upStation) || this.downStation.equals(section.downStation);
    }

    public List<Section> mergeWith(Section section) {
        validateLongerDistanceThan(section);

        if (isOnlyUpStationMatch(section)) {
            return mergeUp(section);
        }

        if (isOnlyDownStationMatch(section)) {
            return mergeDown(section);
        }

        throw new IllegalArgumentException("추가할 구간의 상행역 하행역이 모두 같거나 모두 다를 수 없습니다. 기존 구간: " + this + " 추가할 구간: " + section);
    }

    private void validateLongerDistanceThan(Section section) {
        if (this.distance <= section.getDistance()) {
            throw new IllegalArgumentException("기존 구간의 길이가 같거나 작습니다");
        }
    }

    private boolean isOnlyUpStationMatch(Section section) {
        return section.upStation.equals(this.upStation) && !section.downStation.equals(
            this.downStation);
    }

    private List<Section> mergeUp(Section section) {
        List<Section> mergedSections = new ArrayList<>();
        mergedSections.add(section);
        mergedSections.add(new Section(
            section.line, section.downStation, this.downStation, this.distance - section.distance));
        return mergedSections;
    }

    private boolean isOnlyDownStationMatch(Section section) {
        return !section.upStation.equals(this.upStation) && section.downStation.equals(
            this.downStation);
    }

    private List<Section> mergeDown(Section section) {
        List<Section> mergedSections = new ArrayList<>();
        mergedSections.add(new Section(
            section.line, this.upStation,  section.upStation, this.distance - section.distance));
        mergedSections.add(section);
        return mergedSections;
    }

    public Section removeMiddleStation(Section section) {
        if (!this.downStation.equals(section.upStation)) {
            throw new IllegalArgumentException("이어지지 않은 구간입니다. 현재 구간: " + this + " 상대 구간: " + section);
        }

        return new Section(this.line, this.upStation, section.downStation, this.distance + section.distance);
    }

    public boolean hasDownStationSameAs(Station station) {
        return station.equals(this.downStation);
    }

    public boolean hasUpStationSameAs(Station station) {
        return station.equals(this.upStation);
    }

    public boolean belongTo(Line line) {
        return this.line.equals(line);
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public int getDistance() {
        return distance;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(id, section.id)
            && Objects.equals(line.getId(), section.line.getId()) && Objects.equals(upStation,
            section.upStation) && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line.getId(), upStation, downStation, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
            "lineId=" + this.line.getId() +
            "upStation=" + upStation +
            ", downStation=" + downStation +
            ", distance=" + distance +
            '}';
    }
}
