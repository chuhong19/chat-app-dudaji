package vn.giabaochatapp.giabaochatappserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.giabaochatapp.giabaochatappserver.data.domains.PostReport;
import vn.giabaochatapp.giabaochatappserver.data.dto.mix.PostReportIdDTO;
import vn.giabaochatapp.giabaochatappserver.data.dto.response.StandardResponse;
import vn.giabaochatapp.giabaochatappserver.data.dto.shortName.PostDTO;
import vn.giabaochatapp.giabaochatappserver.services.PostReportService;

import java.util.List;

@RestController
@RequestMapping(value = "/mod")
@PreAuthorize("hasAuthority('MOD')")
public class ModController {

    @Autowired
    public PostReportService postReportService;

    @GetMapping("/viewAllReportedPosts")
    public StandardResponse viewAllReportedPosts() {
        List<PostDTO> reportedPosts = postReportService.viewAllReportedPosts();
        return StandardResponse.create("200", "List reported posts", reportedPosts);
    }

    @PostMapping("/banPost")
    public StandardResponse banPost(@RequestBody PostReportIdDTO postReportIdDTO) {
        PostReport postReport = postReportService.banPost(postReportIdDTO.getPostReportedId());
        return StandardResponse.create("200", "Post banned", postReport);
    }

    @GetMapping("/viewAllBannedPosts")
    public StandardResponse viewAllBannedPosts() {
        List<PostDTO> bannedPosts = postReportService.viewAllBannedPosts();
        return StandardResponse.create("200", "List banned posts", bannedPosts);
    }

}
