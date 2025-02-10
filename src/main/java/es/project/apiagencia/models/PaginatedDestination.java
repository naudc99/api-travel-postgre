package es.project.apiagencia.models;

import java.util.ArrayList;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedDestination {
    private List<DestinationDTO> items = new ArrayList<>();
    private long totalCount;
}
