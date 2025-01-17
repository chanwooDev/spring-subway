package subway.ui;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.application.SectionsService;
import subway.dto.request.LineCreationRequest;
import subway.dto.request.LineUpdateRequest;
import subway.dto.response.LineResponse;

import java.net.URI;
import java.util.List;
import subway.dto.request.SectionAdditionRequest;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final SectionsService sectionsService;

    public LineController(LineService lineService, SectionsService sectionsService) {
        this.lineService = lineService;
        this.sectionsService = sectionsService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid LineCreationRequest lineCreationRequest) {
        LineResponse line = lineService.saveLine(lineCreationRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody @Valid LineUpdateRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> addSection(@PathVariable Long id, @RequestBody @Valid SectionAdditionRequest sectionRequest) {
        sectionsService.addSection(id, sectionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @RequestParam Long stationId) {
        sectionsService.removeLast(id, stationId);
        return ResponseEntity.noContent().build();
    }
}
