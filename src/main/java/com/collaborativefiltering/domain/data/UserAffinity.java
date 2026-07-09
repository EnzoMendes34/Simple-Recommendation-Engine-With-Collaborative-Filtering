package com.collaborativefiltering.domain.data;

import com.collaborativefiltering.domain.model.Category;
import com.collaborativefiltering.domain.model.User;

public record UserAffinity(User user, Category category) {
}
